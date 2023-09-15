(function($, $document) {
    var CF_MODEL_URL = "/mnt/overlay/dam/cfm/models/editor/content/editor.html",
        MF_RES_TYPE = "granite/ui/components/coral/foundation/form/multifield",
        EAEM_SUB_TYPE_PHOTO_GALLERY = "EAEM_PHOTO_GALLERY",
        EAEM_SUB_TYPE_PHOTO_GALLERY_LABEL = "Include Caption and Alt fields",
        EAEM_SUB_TYPE_CB_SUFFIX = "EaemSubType";

    if(isModelEditor()){
        extendModelEditor();
    }

    function extendModelEditor(){
        $document.on("click", "#form-fields > li", addSubTypeCheckBox);

        $document.on("change", "coral-select", handleCoralSelectInModel);
    }

    function handleCoralSelectInModel(){
        var $select = $(this);

        if(this.value != MF_RES_TYPE){
            getPhotoGalleryCheckBox($select).remove();
            return;
        }

        var cbName = $select.attr("name") + EAEM_SUB_TYPE_CB_SUFFIX;

        $(getSubtypeHtml(cbName)).appendTo($select.closest(".coral-Form-fieldwrapper"));
    }

    function addSubTypeCheckBox(){
        var $fieldProperties = $("#" + $(this).data("id") + "-properties"),
            $multiFieldsConfig = $fieldProperties.find("input[value='" + MF_RES_TYPE + "']"),
            $mField, cbName, $cb, url;

        _.each($multiFieldsConfig, function(mField){
            $mField = $(mField);

            cbName = $mField.attr("name") + EAEM_SUB_TYPE_CB_SUFFIX;

            if(!_.isEmpty(getPhotoGalleryCheckBox($mField))){
                return;
            }

            $cb = $(getSubtypeHtml(cbName)).appendTo($mField.closest(".coral-Form-fieldwrapper"));

            url = $mField.closest("form").attr("action") + "/jcr:content/model/cq:dialog/"
                + mField.name.substring(0, mField.name.lastIndexOf("/")) + ".json";

            enablePhotoGalleryCheckBox(cbName, $cb, url);
        });
    }

    function enablePhotoGalleryCheckBox(cbName, $cb, url){
        $.ajax({url: url, async: false}).done(function (data) {
            if (_.isEmpty(data[cbName.substring(cbName.lastIndexOf("/") + 1)])) {
                return;
            }

            $cb[0].checked = true;
        });
    }

    function getPhotoGalleryCheckBox($mField){
        var cbName = $mField.attr("name") + EAEM_SUB_TYPE_CB_SUFFIX;

        return $mField.closest(".coral-Form-fieldwrapper").find("[name='" + cbName + "']");
    }

    function getSubtypeHtml(cbName){
        return  '<coral-checkbox class="coral-Form-field" name="' + cbName + '" value="' + EAEM_SUB_TYPE_PHOTO_GALLERY + '">' +
                    EAEM_SUB_TYPE_PHOTO_GALLERY_LABEL +
                '</coral-checkbox>';
    }

    function isModelEditor(){
        return window.location.pathname.startsWith(CF_MODEL_URL);
    }
})(jQuery, jQuery(document));

(function($, $document) {
    var CF_MODEL_URL = "/mnt/overlay/dam/cfm/models/editor/content/editor.html",
        EAEM_SUB_TYPE_PHOTO_GALLERY = "EAEM_PHOTO_GALLERY",
        MASTER = "master",
        CFM_EDITOR_SEL = ".content-fragment-editor",
        MF_SELECTOR = "coral-multifield",
        EAEM_CARD_CAPTION = "eaem-card-caption",
        EAEM_SUB_TYPE_CB_SUFFIX = "EaemSubType",
        EAEM_CAPTION = "eaem-caption",
        EAEM_ALT = "eaem-alt",
        EAEM_SIZE = "eaem-size",
        photoGalleryAutocompletes = {},
        CF_MODEL = "";

    if(!isModelEditor()){
        window.Dam.CFM.Core.registerReadyHandler(extendFragmentEditor);
    }

    function extendFragmentEditor(){
        extendAutoCompletes();

        extendRequestSave();
    }

    function extendAutoCompletes(){
        loadModelUrl();

        $(MF_SELECTOR).on("coral-collection:add", function(event){
            Coral.commons.ready(event.detail.item, addImageCard);
        });

        /* ==== MRV Team Remove 20230130 START ==== */
        // Fix : Disappearing text values in switching the sort orders of productAssets on CF editor
        // $(MF_SELECTOR).on("coral-collection:remove", removeImageCard);
        /* ==== MRV Team Remove 20230130 END ==== */

        $(MF_SELECTOR).each(function(index, mField){
            Coral.commons.ready(mField, loadPhotoGalleryImages);
        })
    }

    function loadPhotoGalleryImages(mField){
        var $multiField = $(mField);

        if(!isPhotoGalleryEnabled($multiField.attr("data-granite-coral-multifield-name"))){
            return;
        }

        _.each($multiField[0].items.getAll(), function(item) {
            var $content = $(item.content),
                $imageReference = $content.find("foundation-autocomplete");

            var mfData = JSON.parse($imageReference.val());

            $imageReference.val(mfData.path);

            showImage.call($imageReference[0]);

            $content.find("input[name='" + EAEM_CAPTION + "']").val(mfData.caption);
            $content.find("input[name='" + EAEM_ALT + "']").val(mfData.alt);
            $content.find("coral-select[name='" + EAEM_SIZE + "'] coral-select-item[value='" + mfData.size + "']").attr("selected",true);
        });
    }

    function addImageCard(mfItem){
        var $imageReference = $(mfItem).find("foundation-autocomplete");

        if(_.isEmpty($imageReference)){
            return;
        }

        if(!isPhotoGalleryEnabled($imageReference.attr("name"))){
            return;
        }

        if(!_.isEmpty($imageReference.val())){
            showImage.call($imageReference[0]);
        }else{
            $(getImageLabel()).insertBefore($imageReference);
            $imageReference.on("change", showImage);
        }
    }

    function isPhotoGalleryEnabled(mfName){
        return photoGalleryAutocompletes[mfName];
    }

    function showImage(){
        var $imageReference = $(this),
            imageUrl = this.value,
            $mfContent = $imageReference.closest("coral-multifield-item-content"),
            $imageCaption = $mfContent.find("." + EAEM_CARD_CAPTION);

        if(_.isEmpty(this.value)){
            $imageCaption.remove();
            return;
        }

        var fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        imageUrl = imageUrl + "/_jcr_content/renditions/cq5dam.thumbnail.319.319.png";

        if(!_.isEmpty($imageCaption)){
            $imageCaption.find("img", imageUrl);
            return;
        }

        $(getCardContent(imageUrl, fileName)).appendTo($mfContent);

        $mfContent.css("margin-bottom", "20px");
    }

    /* ==== MRV Team Remove 20230130 START ==== */
    /* function removeImageCard(event){
        var $mfItem = $(event.detail.item);

        $mfItem.find("." + EAEM_CARD_CAPTION).remove();
    } */
    /* ==== MRV Team Remove 20230130 END ==== */

    function loadModelUrl(){
        $.ajax({url: window.Dam.CFM.EditSession.fragment.urlBase + "/jcr:content/data.json", async: false}).done(function (data) {
            CF_MODEL = data["cq:model"];
        });

        $.ajax({url: CF_MODEL + "/jcr:content/model/cq:dialog/content/items.1.json", async: false}).done(handler);

        function handler(data){
            var subType;

            _.each(data, function(value){
                if(!_.isObject(value)){
                    return;
                }

                subType = value["sling:resourceType" + EAEM_SUB_TYPE_CB_SUFFIX];

                photoGalleryAutocompletes[value["name"]] = (subType === EAEM_SUB_TYPE_PHOTO_GALLERY);
            })
        }
    }

    function getCardContent(imageUrl, fileName){
        return  '<div class="' + EAEM_CARD_CAPTION + '">' +
                    '<div>' +
                        '<coral-card fixedwidth assetwidth="200" assetheight="200">' +
                            '<coral-card-asset><img src="' + imageUrl + '"></coral-card-asset>' +
                            '<coral-card-content>' +
                                '<coral-card-title>' + fileName + '</coral-card-title>' +
                            '</coral-card-content>' +
                        '</coral-card>' +
                    '</div>' +
                    '<div>' +
                        '<label class="coral-Form-fieldlabel"> Caption</label>' +
                        '<input is="coral-textfield" class="coral-Form-field" name="' + EAEM_CAPTION + '" placeholder="Enter caption" >' +
                    '</div>' +
                    '<div>' +
                        '<label class="coral-Form-fieldlabel"> Alt</label>' +
                        '<input is="coral-textfield" class="coral-Form-field" name="' + EAEM_ALT + '" placeholder="Enter altn" >' +
                    '</div>' +
                    '<div>' +
                        '<label class="coral-Form-fieldlabel"> Size NPR</label>' +
                        '<coral-select class="coral-Form-field" placeholder="Select one" name="' + EAEM_SIZE + '" labelledby="label-aligned-0">' +
                            '<coral-select-item value="small">Small</coral-select-item>' +
                            '<coral-select-item value="middle">Middle</coral-select-item>' +
                            '<coral-select-item value="large">Large</coral-select-item>' +
                        '</coral-select>' +
                    '</div>' +
                '</div>'

    }

    function getImageLabel(){
        return '<label class="coral-Form-fieldlabel"> Image</label>';
    }

    function getPhotoGalleryData(){
        var mfData = {}, values, $fields;

        _.each(photoGalleryAutocompletes, function(isPhotoGallery, refName){
            if(!isPhotoGallery){
                return;
            }

            var $multiField = $("coral-multifield[data-granite-coral-multifield-name='" + refName + "']");

            if(_.isEmpty($multiField)){
                return;
            }

            mfData[refName] = [];

            _.each($multiField[0].items.getAll(), function(item) {
                var $content = $(item.content), data = {};

                data["path"] = $content.find("input[name='" + refName + "']").val();
                data["caption"] = $content.find("input[name='" + EAEM_CAPTION + "']").val();
                data["alt"] = $content.find("input[name='" + EAEM_ALT + "']").val();
                data["size"] = $content.find("coral-select[name='" + EAEM_SIZE + "']").val();

                mfData[refName].push(JSON.stringify(data));
            });
        });

        return mfData;
    }

    function extendRequestSave(){
        var CFM = window.Dam.CFM,
            orignFn = CFM.editor.Page.requestSave;

        CFM.editor.Page.requestSave = requestSave;

        function requestSave(callback, options) {
            orignFn.call(this, callback, options);

            var mfsData = getPhotoGalleryData();

            if(_.isEmpty(mfsData)){
                return;
            }

            var url = CFM.EditSession.fragment.urlBase + ".cfm.content.json",
                variation = getVariation(),
                createNewVersion = (options && !!options.newVersion) || false;

            var data = {
                ":type": "multiple",
                ":newVersion": createNewVersion,
                "_charset_": "utf-8"
            };

            if(variation !== MASTER){
                data[":variation"] = variation;
            }

            var request = {
                url: url,
                method: "post",
                dataType: "json",
                data: _.merge(data, mfsData),
                cache: false
            };

            CFM.RequestManager.schedule({
                request: request,
                type: CFM.RequestManager.REQ_BLOCKING,
                condition: CFM.RequestManager.COND_EDITSESSION,
                ui: (options && options.ui)
            })
        }
    }

    function getVariation(){
        var variation = $(CFM_EDITOR_SEL).data('variation');

        variation = variation || "master";

        return variation;
    }

    function isModelEditor(){
        return window.location.pathname.startsWith(CF_MODEL_URL);
    }
})(jQuery, jQuery(document));
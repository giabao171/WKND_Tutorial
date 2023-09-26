import $ from "jquery"

const renderPrice = () => {
	$(".cmp-product_panel__price-txt").each(function() {
		$(this).html($(this).data("product-price"))
	})
}

$(".cmp-product_panel__icon-fav-btn").on("click", function(e) {
	e.stopPropagation();
	$(this).css("z-index", "-1");
	$(this).closest(".cmp-product_panel__icon-fav").addClass("show-before");
})

$(".cmp-product_panel__icon-fav").before().on("click", function() {
	let $parent = $(this).closest(".cmp-product_panel__icon-fav");
	if ($parent.hasClass("show-before"))
		$parent.removeClass("show-before");
	$parent.find(".cmp-product_panel__icon-fav-btn").css("z-index", "3")
});

renderPrice();






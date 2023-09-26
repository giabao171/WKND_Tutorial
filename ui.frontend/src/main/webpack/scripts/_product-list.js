import { _panelListSlide } from "./_common-slide.js"
import SimpleBar from 'simplebar';
import $ from "jquery"

window.addEventListener("DOMContentLoaded", function() {
	
	Array.prototype.forEach.call(document.getElementsByClassName("cmp-product_list__wrapper"), item => {
		let componentTye = "cmp-product_list";
		let simpleBarContentEl = "";
		if($(item).children(".cmp-product_list__frame").length > 0) {
			let slider = new SimpleBar($(item).find(".cmp-product_list__frame")[0], {autoHide: false});
			simpleBarContentEl = slider.getScrollElement();
		
			new _panelListSlide(item, componentTye, simpleBarContentEl)
		}
	})
})

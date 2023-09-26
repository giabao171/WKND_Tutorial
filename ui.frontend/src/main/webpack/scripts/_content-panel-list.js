import { _panelListSlide } from "./_common-slide";
import SimpleBar from 'simplebar';
import $ from "jquery"

window.addEventListener('DOMContentLoaded', () => {
  Array.prototype.forEach.call(document.getElementsByClassName('cmp-content-panel_list__wrapper'), target => {
    var componentType = 'cmp-content-panel_list';
    var simpleBarContentEl;
    if ($(target).closest(".cmp-content-panel-list--slider").length > 0 ||
        (window.screen.width > 767 & $(target).closest(".cmp-content-panel-list--list").length > 0)) {
      var simpleBar = new SimpleBar($(target).find(".cmp-content-panel_list__frame")[0], {
        autoHide: false,
      })
      simpleBarContentEl = simpleBar.getScrollElement();
    }
    new _panelListSlide(target, componentType, simpleBarContentEl);
  });
}, false);

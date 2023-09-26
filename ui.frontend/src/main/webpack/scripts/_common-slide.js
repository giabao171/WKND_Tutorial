import $ from "jquery"

export function _panelListSlide(el, componentType, simpleBarContentEl) {
  this.el = el;
  this.componentType = componentType
  this.display = null;
  this.simpleBarContentEl = simpleBarContentEl;
  this.init();
}
_panelListSlide.prototype = {
  init: function() {
    this.rtl = document.getElementsByClassName('is-lang-rtl').length;
    if(!this.rtl){
      const lang = document.documentElement.lang;
      if (lang.includes('ar') || lang.includes('ar-AE') || lang.includes('fa')) {
          this.rtl = true;
      }
    }
    if (this.componentType == 'cmp-tabs') {
		this.frame = this.el.getElementsByClassName('cmp-tabs__tablist')[0];
		this.items = this.el.getElementsByClassName('cmp-tabs__tablist')[0];
		this.item = this.el.getElementsByClassName('cmp-tabs__tab');
    } else {
        if (this.simpleBarContentEl) {
            this.frame = this.simpleBarContentEl;
        } else {
            this.frame = this.el.getElementsByClassName(this.componentType + '__frame')[0];
        }
        this.items = this.el.getElementsByClassName(this.componentType + '__items')[0];
        this.item = this.el.getElementsByClassName(this.componentType + '__item');
    }
    this.prev = this.el.getElementsByClassName(this.componentType + `__btn-${!!this.rtl ? 'next' : 'prev'}`)[0];
    this.next = this.el.getElementsByClassName(this.componentType + `__btn-${!this.rtl ? 'next' : 'prev'}`)[0];
    if(!this.frame || !this.prev || !this.next || !this.items || !this.item) return false;
    if(!this.active()) return false;
    this.resize();
    this.bind();
  },
  active: function() {
    return this.frame.clientWidth >= this.items.scrollLeft;
  },
  bind: function() {
    window.addEventListener('resize', this.resize.bind(this), false);
    this.el.addEventListener('click', this.control.bind(this), false);
    this.frame.addEventListener('scroll', this.scrollHandler.bind(this), false);
  },
  resize: function(){
    const update = window.innerWidth < 768 ? 'sp' : 'pc';
    if(this.display !== update) {
      this.display = update;
      this.destory();
    }
    this.scrollHandler();
  },
  destory: function() {
    if(this.display === 'sp') {
      this.prev.style.display = 'none';
      this.next.style.display = 'none';
    }
  },
  control: function(ev) {
    if(this.display === 'sp') return false;
    let tgt = ev.target;
    do{
      if(!tgt || tgt === ev.currentTarget) break;
      if(tgt === this.prev) {
        this.slide(-1);
        break;
      } else if(tgt === this.next) {
        this.slide(1);
        break;
      }
      tgt = tgt.parentNode;
    } while(tgt);
  },
  slide: function(dis) {
    if(this.display === 'sp') return false;
    const right = !!this.rtl ? 'left' : 'right', left = right === 'right' ? 'left' : 'right';
    let tgt = null, ammount = 0;
    if (this.rtl) {
      if(dis === 1) {
        Array.prototype.forEach.call(this.item, item => {
          if(Math.ceil(item.getBoundingClientRect()[right]) < Math.floor(this.frame.getBoundingClientRect()[right])) {
            if(!tgt) tgt = item;
          }
        });
        ammount = Math.ceil(this.frame.scrollLeft) + Math.ceil(tgt.getBoundingClientRect()[right]) - (Math.floor(this.frame.getBoundingClientRect()[left]) + 1 - this.frame.clientWidth );
      } else {
        Array.prototype.forEach.call(this.item, item => {
          if(Math.ceil(item.getBoundingClientRect()[left]) > Math.ceil( this.frame.getBoundingClientRect()[left])) {
            tgt = item;
          }
        });
        ammount = Math.ceil(this.frame.scrollLeft) + Math.ceil(tgt.getBoundingClientRect()[left]) - (Math.floor(this.frame.getBoundingClientRect()[left]));
      }
    } else {
      if(dis === 1) {
        Array.prototype.forEach.call(this.item, item => {
          if(Math.ceil(this.frame.getBoundingClientRect()[right] < Math.floor(item.getBoundingClientRect()[right]))) {
            if(!tgt) {
				tgt = item;
			}
          }
        });
        ammount = Math.ceil(this.frame.scrollLeft) + Math.ceil(tgt.getBoundingClientRect()[right]) - Math.floor(this.frame.getBoundingClientRect()[right]) + 1;
      } else {
        Array.prototype.forEach.call(this.item, item => {
          if(Math.ceil(this.frame.getBoundingClientRect()[left] > Math.ceil(item.getBoundingClientRect()[left]))) {
            tgt = item;              
          }
        });
        ammount = Math.ceil(this.frame.scrollLeft) - Math.ceil(this.frame.getBoundingClientRect()[left]) + Math.floor(tgt.getBoundingClientRect()[left]) - 1;
      }
    }
	$(this.frame).animate({ scrollLeft: ammount }, 480);
  },
  scrollHandler: function() {
    if(this.display === 'sp') return false;
    if(this.frame.clientWidth === Math.max(this.items.scrollWidth, this.items.clientWidth)) {
      this.prev.style.display = 'none';
      this.next.style.display = 'none';
    } else if((!this.rtl ? Math.ceil(this.frame.scrollLeft) : 0) <= (!!this.rtl ? Math.ceil(this.frame.scrollLeft) : 0)) {
      this.prev.style.display = 'none';
      this.next.style.display = 'block';
    } else if(Math.ceil(this.frame.scrollLeft) * (this.rtl ? -1 : 1) + this.frame.clientWidth >= this.items.scrollWidth) {
      this.prev.style.display = 'block';
      this.next.style.display = 'none';
    } else {
      this.prev.style.display = 'block';
      this.next.style.display = 'block';
    }
  }
};
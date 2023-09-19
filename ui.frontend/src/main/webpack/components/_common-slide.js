import $ from "jquery";

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
		console.log("this.rtl in init() !this.rtl: ",this.rtl)
      const lang = document.documentElement.lang;
      if (lang.includes('ar') || lang.includes('ar-AE') || lang.includes('fa')) {
          this.rtl = true;
          console.log("this.rtl in init() in if: ",this.rtl)
      }
      console.log("this.rtl in init() out if: ",this.rtl)
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
    console.log(this.frame.clientWidth + " >= " + this.items.scrollLeft)
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
    console.log("ev.target in control: ", ev.target)
    do{
      if(!tgt || tgt === ev.currentTarget) break;
      if(tgt === this.prev) {
        this.slide(-1);
        console.log("prev: " + tgt + " : " + this.prev)
        break;
      } else if(tgt === this.next) {
        this.slide(1);
        console.log("next")
        console.log("next: " + tgt + " : " + this.next)
        break;
      }
      tgt = tgt.parentNode;
    } while(tgt);
  },
  slide: function(dis) {
    if(this.display === 'sp') return false;
    const right = !!this.rtl ? 'left' : 'right', left = right === 'right' ? 'left' : 'right';
    console.log("this.rtl in slide() : ",this.rtl)
    let tgt = null, ammount = 0;
    console.log(this.frame.getBoundingClientRect()[left])
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
			console.log("frame right", item.getBoundingClientRect()[right])
            console.log("item right", Math.ceil(item.getBoundingClientRect()[right]))
          if(Math.ceil(this.frame.getBoundingClientRect()[right] < Math.floor(item.getBoundingClientRect()[right]))) {
            if(!tgt) {
				tgt = item;
				console.log("!tgt :", !tgt)
			}
            /*console.log("frame right", item.getBoundingClientRect()[right])
            console.log("item right", Math.ceil(item.getBoundingClientRect()[right]))*/
            console.log("<")
          }
        });
        ammount = Math.ceil(this.frame.scrollLeft) + Math.ceil(tgt.getBoundingClientRect()[right]) - Math.floor(this.frame.getBoundingClientRect()[right]) + 1;
      	console.log("amount this.frame.scrollLeft", this.frame.scrollLeft)
        console.log("this.frame.getBoundingClientRect()[left]", this.frame.getBoundingClientRect()[right])
        console.log("tgt.getBoundingClientRect()[left]", tgt.getBoundingClientRect()[right])
      } else {
        Array.prototype.forEach.call(this.item, item => {
			console.log("frame left", item.getBoundingClientRect()[left])
            console.log("item left", Math.ceil(item.getBoundingClientRect()[left]))
          if(Math.ceil(this.frame.getBoundingClientRect()[left] > Math.ceil(item.getBoundingClientRect()[left]))) {
            tgt = item;
            /*console.log("frame left", item.getBoundingClientRect()[left])
            console.log("item left", Math.ceil(item.getBoundingClientRect()[left]))*/
            console.log(">")
          }
        });
        ammount = Math.ceil(this.frame.scrollLeft) - Math.ceil(this.frame.getBoundingClientRect()[left]) + Math.floor(tgt.getBoundingClientRect()[left]) - 1;
    	console.log("amount this.frame.scrollLeft", this.frame.scrollLeft)
        console.log("this.frame.getBoundingClientRect()[left]", this.frame.getBoundingClientRect()[left])
        console.log("tgt.getBoundingClientRect()[left]", tgt.getBoundingClientRect()[left])
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
      console.log("prev: none ",Math.ceil(this.frame.scrollLeft), " ", Math.ceil(this.frame.scrollLeft))
    } else if(Math.ceil(this.frame.scrollLeft) * (this.rtl ? -1 : 1) + this.frame.clientWidth >= this.items.scrollWidth) {
      this.prev.style.display = 'block';
      this.next.style.display = 'none';
      console.log("next: none ",this.frame.scrollLeft, " ", this.frame.clientWidth, " ", this.items.scrollWidth)
    } else {
      this.prev.style.display = 'block';
      this.next.style.display = 'block';
    }
  }
};
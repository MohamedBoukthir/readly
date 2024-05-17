import {Directive, ElementRef, Input, AfterViewInit, numberAttribute} from '@angular/core';

@Directive({
  selector: '[appReadMore]'
})
export class ReadMoreDirective implements AfterViewInit {
  @Input({transform: numberAttribute, alias: "appReadMore"}) limit: number = 100;

  constructor(private el: ElementRef) { }

  ngAfterViewInit() {
    let text = this.el.nativeElement.innerText;
    if (text.length > this.limit) {
      let shortText = text.substring(0, this.limit) + '... ';
      let readMore = document.createElement('a');
      readMore.textContent = 'Read More';

      // Add styles to the 'Read More' text
      readMore.style.color = 'black';
      readMore.style.cursor = 'pointer';
      readMore.style.fontWeight = '500';

      let readLess = document.createElement('a');
      readLess.textContent = ' Show Less';

      // Add styles to the 'Show Less' text
      readLess.style.color = 'black';
      readLess.style.cursor = 'pointer';
      readLess.style.fontWeight = '500';

      readMore.addEventListener('click', () => {
        this.el.nativeElement.innerText = text;
        this.el.nativeElement.appendChild(readLess);
      });

      readLess.addEventListener('click', () => {
        this.el.nativeElement.innerText = shortText;
        this.el.nativeElement.appendChild(readMore);
      });

      this.el.nativeElement.innerText = shortText;
      this.el.nativeElement.appendChild(readMore);
    }
  }
}

import {AfterViewInit, Directive, ElementRef, Input} from '@angular/core';

@Directive({
  selector: '[appNgOptimizedImage]'
})
export class NgOptimizedImageDirective implements AfterViewInit{
  @Input('appNgOptimizedImage') src!: string;

  constructor(
    private el: ElementRef,

  ) { }

  ngAfterViewInit() {
    const observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          this.el.nativeElement.src = this.src;
          observer.disconnect();
        }
      });
    });

    observer.observe(this.el.nativeElement);
  }

}

import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-rate',
  templateUrl: './rate.component.html',
  styleUrl: './rate.component.css'
})
export class RateComponent {

  @Input() rate: number = 0;
  maxRate: number = 5;

  get fullStars(): number {
    return Math.floor(this.rate);
  }

  get hasHalfStar(): boolean {
    return this.rate % 1 !== 0;
  }

  get emptyStars(): number {
    return this.maxRate - Math.ceil(this.rate);
  }

}

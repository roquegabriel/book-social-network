import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-rating',
  imports: [CommonModule],
  templateUrl: './rating.component.html',
  styleUrl: './rating.component.scss'
})
export class RatingComponent {
  
  @Input() rating: number = 0;
  maxRating: number = 5;
  
  public get fullStars() : number {
    return Math.floor(this.rating);
  }
  
  public get hasHalfStar() : boolean {
    return this.rating % 1 !== 0;
  }
  
  public get emptyStars() : number {
    return this.maxRating - Math.ceil(this.rating);
  }
}

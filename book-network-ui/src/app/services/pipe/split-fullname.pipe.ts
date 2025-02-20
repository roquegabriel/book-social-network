import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'splitFullname'
})
export class SplitFullnamePipe implements PipeTransform {

  transform(value: string): string {
    const fullName = value.split(' ');    
      return fullName[0];
  }

}
import { Pipe, PipeTransform } from '@angular/core';
/**
 * Created by gFolgoas on 29/01/2019.
 */
@Pipe({ name: 'stringTruncate' })
export class StringTruncatePipe implements PipeTransform {
    transform(value: string, limit: number): string {
        if (value == null || typeof value === 'undefined') {
            return '';
        }
        return value.length > limit ? value.substring(0, limit).concat('...') : value;
    }
}

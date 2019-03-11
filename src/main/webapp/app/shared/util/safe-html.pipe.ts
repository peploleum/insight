/**
 * Created by gFolgoas on 11/03/2019.
 */
import { DomSanitizer } from '@angular/platform-browser';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'insSafeHtml' })
export class SafeHtmlPipe implements PipeTransform {
    constructor(private sanitized: DomSanitizer) {}

    transform(value) {
        return this.sanitized.bypassSecurityTrustHtml(value);
    }
}

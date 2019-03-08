/**
 * Created by gFolgoas on 06/03/2019.
 */
import { AfterViewInit, Directive, ElementRef, Input, OnChanges, OnDestroy, Renderer2 } from '@angular/core';
import { EntityPosition, RawData } from '../model/raw-data.model';
/**
 * Remplace des éléments du texte brut par des liens vers les entités correspondantes.
 *
 */
@Directive({
    selector: '[insRawdataAnnotationDirective]'
})
export class RawdataAnnotationDirective implements AfterViewInit, OnChanges, OnDestroy {
    @Input()
    data: any;
    entitiesPositions: EntityPosition[];
    rawTextContent: string;

    constructor(private _el: ElementRef, private _renderer: Renderer2) {}

    ngOnChanges(changes: any) {
        if (this.data !== null && typeof this.data !== 'undefined') {
            const rawDataAnnotations: string =
                this.data['rawDataAnnotations'] || this.data instanceof Map ? this.data.get('rawDataAnnotations') : null;
            if (rawDataAnnotations) {
                try {
                    this.entitiesPositions = JSON.parse(rawDataAnnotations) || [];
                } catch (e) {
                    console.log('RawdataAnnotationDirective: Bad JSON to parse');
                }
            }
        }
    }

    ngAfterViewInit() {
        this.rawTextContent = this._el.nativeElement.innerText;
        this.setAnnotations();
    }

    ngOnDestroy() {}

    setAnnotations() {
        if (this.rawTextContent && this.entitiesPositions) {
            let newTxt = `${this.rawTextContent}`;
            this.entitiesPositions.forEach((pos: EntityPosition) => {
                const entityLength: number = pos.entityWord.length;
                if (newTxt.length - 3 > pos.position + entityLength) {
                    newTxt =
                        newTxt.substring(0, pos.position) +
                        this.getEntityLink(pos.entityWord, pos.entityType) +
                        newTxt.substring(pos.position + entityLength);
                    console.log(newTxt);
                }
            });
            this._renderer.setProperty(this._el.nativeElement, 'innerHTML', newTxt);
        }
    }

    getEntityLink(entityWord: string, entityType: string): string {
        return `<a style="color:${this.getEntityColor(entityType)};">${entityWord.toString()}</a>`;
    }

    getEntityColor(objectType: string): string {
        switch (objectType) {
            case 'Biographics':
                return 'red';
                break;
            case 'Event':
                return 'blue';
                break;
            case 'Equipment':
                return 'green';
                break;
            case 'RawData':
                return 'pink';
                break;
            case 'Location':
                return 'red';
                break;
            default:
                return '';
        }
    }
}

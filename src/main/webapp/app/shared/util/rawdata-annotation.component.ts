/**
 * Created by gFolgoas on 07/03/2019.
 */
import { AfterViewInit, Component, Input, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { EntityPosition } from '../model/raw-data.model';

@Component({
    selector: 'ins-rawdata-annotation',
    templateUrl: './rawdata-annotation.component.html',
    styles: []
})
export class RawdataAnnotationComponent implements OnChanges, OnInit, OnDestroy, AfterViewInit {
    @Input()
    data: any;
    @Input()
    rawTextContent: string;
    entitiesPositions: EntityPosition[];

    innerHTML: any;

    constructor() {}

    ngOnChanges(changes: any) {
        if (this.data !== null && typeof this.data !== 'undefined') {
            const rawDataAnnotations: string =
                this.data['rawDataAnnotations'] || this.data instanceof Map ? this.data.get('rawDataAnnotations') : null;
            if (rawDataAnnotations) {
                try {
                    this.entitiesPositions = JSON.parse(rawDataAnnotations) || [];
                    this.setAnnotations();
                } catch (e) {
                    console.log('RawdataAnnotationDirective: Bad JSON to parse');
                }
            }
        }
    }

    ngAfterViewInit() {}

    ngOnInit() {}

    ngOnDestroy() {}

    setAnnotations() {
        if (this.entitiesPositions) {
            let newTxt = `${this.rawTextContent}`;
            let addedCaractersNumber = 0;
            this.entitiesPositions.forEach((pos: EntityPosition) => {
                const entityLength: number = pos.entityWord.length;
                const entityPosition = pos.position + addedCaractersNumber;
                if (newTxt.length + addedCaractersNumber - 3 > entityPosition + entityLength) {
                    const entityReplace: string = this.getEntityLink(pos.entityWord, pos.entityType);
                    newTxt = newTxt.substring(0, entityPosition) + entityReplace + newTxt.substring(entityPosition + entityLength);
                    addedCaractersNumber += entityReplace.length - 1;
                }
            });
            this.innerHTML = newTxt;
        }
    }

    getEntityLink(entityWord: string, entityType: string): string {
        return `<a style="color:${this.getEntityColor(entityType)} !important;">${entityWord.toString()}</a>`;
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

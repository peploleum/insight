/**
 * Created by gFolgoas on 07/03/2019.
 */
import { AfterViewInit, Component, Input, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { EntityPosition } from '../model/raw-data.model';
import { UUID } from '../util/insight-util';

@Component({
    selector: 'ins-rawdata-hyperlink',
    templateUrl: './rawdata-hyperlink.component.html',
    styles: []
})
export class RawdataHyperlinkComponent implements OnChanges, OnInit, OnDestroy, AfterViewInit {
    @Input()
    data: any;
    @Input()
    rawTextContent: string;

    @Input()
    textLimit: number;
    @Input()
    useExpander: boolean;

    entitiesPositions: EntityPosition[];
    uniqueHyperlinkIds: Map<string, EntityPosition> = new Map();
    innerHTML: any;

    constructor() {}

    ngOnChanges(changes: any) {
        if (this.data !== null && typeof this.data !== 'undefined') {
            const rawDataAnnotations: string = this.data['rawDataAnnotations']
                ? this.data['rawDataAnnotations']
                : this.data instanceof Map
                ? this.data.get('rawDataAnnotations')
                : null;
            if (rawDataAnnotations) {
                try {
                    this.entitiesPositions = JSON.parse(rawDataAnnotations) || [];
                    this.entitiesPositions.sort((a, b) => {
                        return a.position < b.position ? -1 : a.position > b.position ? 1 : 0;
                    });
                    this.setAnnotations();
                } catch (e) {
                    console.log('RawdataAnnotationDirective: Bad JSON to parse');
                }
            } else {
                this.innerHTML = this.rawTextContent;
            }
        }
    }

    ngAfterViewInit() {}

    ngOnInit() {}

    ngOnDestroy() {}

    setAnnotations() {
        if (this.entitiesPositions) {
            let newTxt = '';
            let lastPosition = 0;
            let index = 0;

            for (const pos of this.entitiesPositions) {
                const uniqueId = UUID();
                this.uniqueHyperlinkIds.set(uniqueId, pos);
                const entityLength: number = pos.entityWord.length;
                const entityPosition = index > 0 ? pos.position + 1 : pos.position;
                const entityReplace: string = this.getEntityLink(pos.entityWord, pos.entityType, uniqueId);

                if (this.rawTextContent.length - 3 > entityPosition + entityLength) {
                    const partText: string = this.rawTextContent.substring(lastPosition, entityPosition) + entityReplace + '';
                    newTxt = newTxt.concat(partText);
                    lastPosition = entityPosition + entityLength;
                    if (index === this.entitiesPositions.length - 1) {
                        const endText: string = this.rawTextContent.substring(lastPosition);
                        newTxt = newTxt.concat(endText);
                    }
                } else {
                    const endText: string = this.rawTextContent.substring(lastPosition);
                    newTxt = newTxt.concat(endText);
                    break;
                }
                index++;
            }
            this.innerHTML = newTxt;
        }
    }

    getEntityLink(entityWord: string, entityType: string, entityUniqueId: string): string {
        return `<a id="${entityUniqueId.toString()}" class="${this.getEntityClass(entityType)}">${entityWord.toString()}</a>`;
    }

    getEntityClass(objectType: string): string {
        switch (objectType) {
            case 'Biographics':
                return 'biographics';
                break;
            case 'Event':
                return 'event';
                break;
            case 'Equipment':
                return 'equipment';
                break;
            case 'RawData':
                return 'rawdata';
                break;
            case 'Location':
                return 'location';
                break;
            default:
                return '';
        }
    }

    coucou(el) {
        console.log(el);
    }
}

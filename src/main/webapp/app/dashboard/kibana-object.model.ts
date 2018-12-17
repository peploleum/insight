import { SafeResourceUrl, DomSanitizer } from '@angular/platform-browser';

export interface IKibanaObjectReference {
    idObject: string;

    getSafeResourceUrl(ds: DomSanitizer, targetUrl: string): SafeResourceUrl;
}

export class KibanaDashboardReference implements IKibanaObjectReference {
    private _filterParameters?: string[];
    private _dashboardSafeUrl?: SafeResourceUrl;
    private _selected?: boolean;

    constructor(public idObject: string) {}

    get filterParameters(): string[] {
        return this._filterParameters;
    }

    set filterParameters(value: string[]) {
        this._filterParameters = value;
    }

    get dashboardSafeUrl(): SafeResourceUrl {
        return this._dashboardSafeUrl;
    }

    set dashboardSafeUrl(value: SafeResourceUrl) {
        this._dashboardSafeUrl = value;
    }

    get selected(): boolean {
        return this._selected;
    }

    set selected(value: boolean) {
        this._selected = value;
    }

    getSafeResourceUrl(ds: DomSanitizer, targetUrl: string): SafeResourceUrl {
        return ds.bypassSecurityTrustResourceUrl(targetUrl + 'app/kibana#/dashboard/' + this.idObject + '?embed=true&_g');
    }
}

export interface IKibanaDashboardGenerationParameters {
    dashboardTitle: string;
    visualisations: KibanaVisualisationGenerationParameters[];
}

export class KibanaDashboardGenerationParameters implements IKibanaDashboardGenerationParameters {
    constructor(public dashboardTitle: string, public visualisations: IKibanaVisualisationGenerationParameters[]) {}
}

export interface IKibanaVisualisationGenerationParameters {
    visualizationTitle: string;
    visualizationType: VisualizationType;

    indexPatternId: string;
    indexPatternName: string;
    indexPatternFieldTarget: string;

    timeFromFilter?: string;
    timeToFilter?: string;
    timeInterval?: string;
}

export class KibanaVisualisationGenerationParameters implements IKibanaVisualisationGenerationParameters {
    constructor(
        public visualizationTitle: string,
        public visualizationType: VisualizationType,
        public indexPatternId: string,
        public indexPatternName: string,
        public indexPatternFieldTarget: string
    ) {}
}

export interface IEntityMappingInfo {
    indexPatternId: string;
    indexPatternName: string;
    fields: EntityFieldMappingInfo[];
}

export class EntityMappingInfo implements IEntityMappingInfo {
    constructor(public indexPatternId: string, public indexPatternName: string, public fields: EntityFieldMappingInfo[]) {}
}

export class EntityFieldMappingInfo {
    private _fieldName: string;
    private _fieldType: EntityFieldType;

    constructor(fieldName: string, fieldType: EntityFieldType) {
        this._fieldName = fieldName;
        this._fieldType = fieldType;
    }

    get fieldName(): string {
        return this._fieldName;
    }

    set fieldName(value: string) {
        this._fieldName = value;
    }

    get fieldType(): EntityFieldType {
        return this._fieldType;
    }

    set fieldType(value: EntityFieldType) {
        this._fieldType = value;
    }
}

export type EntityFieldType = 'STRING' | 'NUMBER' | 'DATE' | 'GEOLOCATION';
export type VisualizationType = 'VISU_TABLE' | 'VISU_PIE' | 'VISU_VERT_BAR' | 'VISU_TIMELINE' | 'VISU_MAP';

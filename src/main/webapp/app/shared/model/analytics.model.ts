import { IBiographics } from 'app/shared/model/biographics.model';

export class AnalysisState {
    isEditing: boolean;

    constructor(isEditing: boolean) {
        this.isEditing = isEditing;
    }
}

export type Theme = 'TER' | 'ESP' | 'SAB' | 'SUB' | 'CRO';

/**
 * Sert uniquement à afficher ou non un élément en HTML
 * */
export interface IDisplayDecorator {
    display?: boolean;
}

export interface IMotClefUrls extends IDisplayDecorator {
    motClef: string;
    listUrlDepth: { url: string; depth: string; scoreImageHit?: string }[];
}

export interface IHitDTO {
    theme: 'TER' | 'ESP' | 'SAB' | 'SUB' | 'CRO';
    motClefUrls: IMotClefUrls[];
}

export interface BiographicsScoreDTO {
    biographic?: IBiographics;
    idDictionary?: string;
    nameDictionary?: string;
    hits?: IHitDTO[];
    scores?: IScoreDTO[];
}

export interface IScoreDTO {
    externalIdBio?: string;
    externalIdRawData?: string;
    mongoIdRawData?: string;
    rawDataUrl?: string;
    scorePoints?: string;
    scoreListMotsClefs?: { theme: Theme; motClef: string }[];
    scoreImageHit?: string;
    scoreFrequence?: string;
    depthLevel?: string;
    idDictionary?: string;
}

export class ScoreDTO implements IScoreDTO {
    constructor(
        public externalIdBio?: string,
        public externalIdRawData?: string,
        public mongoIdRawData?: string,
        public rawDataUrl?: string,
        public scorePoints?: string,
        public scoreListMotsClefs?: { theme: Theme; motClef: string }[],
        public scoreImageHit?: string,
        public scoreFrequence?: string,
        public depthLevel?: string,
        public idDictionary?: string
    ) {}
}

export interface IDictionary {
    id?: string;
    name?: string;
    theme?: ThemeDTO[];
}

export class Dictionary implements IDictionary {
    constructor(public id?: string, public name?: string, public theme?: ThemeDTO[]) {}
}

export interface IThemeDTO {
    name?: Theme;
    motclef?: MotclefDTO[];
}

export class ThemeDTO implements IThemeDTO {
    constructor(public name?: Theme, public motclef?: MotclefDTO[]) {}
}

export interface IMotclefDTO {
    clef?: string;
    pond?: number;
}

export class MotclefDTO implements IMotclefDTO {
    constructor(public clef?: string, public pond?: number) {}
}

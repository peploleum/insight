import { IBiographics } from 'app/shared/model/biographics.model';

export class AnalysisState {
    isEditing: boolean;

    constructor(isEditing: boolean) {
        this.isEditing = isEditing;
    }
}

export type Theme = 'TER' | 'ESP' | 'SAB' | 'SUB' | 'CRO';

export interface IHitDTO {
    theme: 'TER' | 'ESP' | 'SAB' | 'SUB' | 'CRO';
    motClefUrls: { motClef: string; urls: string[] }[];
}

export interface BiographicsScoreDTO {
    biographic?: IBiographics;
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
        public scoreFrequence?: string
    ) {}
}

export interface IDictionary {
    id?: string;
    theme?: ThemeDTO[];
}

export class Dictionary implements IDictionary {
    constructor(public id?: string, public theme?: ThemeDTO[]) {}
}

export interface IThemeDTO {
    name?: string;
    motclef?: MotclefDTO[];
}

export class ThemeDTO implements IThemeDTO {
    constructor(public name?: string, public motclef?: MotclefDTO[]) {}
}

export interface IMotclefDTO {
    clef?: string;
    pond?: string;
}

export class MotclefDTO implements IMotclefDTO {
    constructor(public clef?: string, public pond?: string) {}
}

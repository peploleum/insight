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
    motsClefs: string[];
}

export interface BiographicsScoreDTO {
    biographic?: IBiographics;
    hits?: IHitDTO[];
    scores?: IScoreDTO[];
}

export interface IScoreDTO {
    idBio?: string;
    scorePoints?: string;
    scoreListMotsClefs?: { theme: Theme; motClef: string }[];
    scoreImageHit?: string;
    scoreFrequence?: string;
}

export class ScoreDTO implements IScoreDTO {
    constructor(
        public idBio?: string,
        public scorePoints?: string,
        public scoreListMotsClefs?: { theme: Theme; motClef: string }[],
        public scoreImageHit?: string,
        public scoreFrequence?: string
    ) {}
}

export interface IDictionaryDTO {
    id?: string;
    themeDTOList?: ThemeDTO[];
}

export class DictionaryDTO implements IDictionaryDTO {
    constructor(public id?: string, public themeDTOList?: ThemeDTO[]) {}
}

export interface IThemeDTO {
    name?: string;
    motclefDTOList?: MotclefDTO[];
}

export class ThemeDTO implements IThemeDTO {
    constructor(public name?: string, public motclefDTOList?: MotclefDTO[]) {}
}

export interface IMotclefDTO {
    clef?: string;
    pond?: string;
}

export class MotclefDTO implements IMotclefDTO {
    constructor(public clef?: string, public pond?: string) {}
}

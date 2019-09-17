import { Moment } from 'moment';
import List = Mocha.reporters.List;
/**
 * Created by gFolgoas on 07/02/2019.
 */
export class AnalysisState {
    isEditing: boolean;

    constructor(isEditing: boolean) {
        this.isEditing = isEditing;
    }
}

export interface IScoreDTO {
    idBio?: string;
    scorePoints?: string;
    scoreListMotsClefs?: string[];
    scoreImageHit?: string;
    scoreFrequence?: string;
}

export class ScoreDTO implements IScoreDTO {
    constructor(
        public idBio?: string,
        public scorePoints?: string,
        public scoreListMotsClefs?: string[],
        public scoreImageHit?: string,
        public scoreFrequence?: string
    ) {}
}

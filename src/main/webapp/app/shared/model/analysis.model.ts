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
    //TODO AJOUTER EXTERNAL_ID
    scorePoints?: string;
    scoreListMotsClefs?: string[];
    scoreImageHit?: string;
    scoreFrequence?: string;
}

export class ScoreDTO implements IScoreDTO {
    constructor(
        public scorePoints?: string,
        public scoreListMotsClefs?: string[],
        public scoreImageHit?: string,
        public scoreFrequence?: string
    ) {}
}

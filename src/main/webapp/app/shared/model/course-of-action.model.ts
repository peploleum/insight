export interface ICourseOfAction {
    id?: number;
    description?: string;
    nom?: string;
    type?: string;
    linkOfNom?: string;
    linkOfId?: number;
}

export class CourseOfAction implements ICourseOfAction {
    constructor(
        public id?: number,
        public description?: string,
        public nom?: string,
        public type?: string,
        public linkOfNom?: string,
        public linkOfId?: number
    ) {}
}

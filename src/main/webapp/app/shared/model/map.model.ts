/**
 * Created by gFolgoas on 18/01/2019.
 */
export interface IMapDataDTO {
    id?: string;
    label?: string;
    objectType?: string;
    description?: string;
    coordinate?: number[];
    properties?: any;
}

export class MapDataDTO implements IMapDataDTO {
    constructor(
        public id?: string,
        public label?: string,
        public objectType?: string,
        public description?: string,
        public coordinate?: number[],
        public properties?: any
    ) {}
}

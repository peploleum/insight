export const enum EquipmentType {
    WEAPON = 'WEAPON',
    TOOL = 'TOOL',
    COMMON = 'COMMON',
    UNKNOWN = 'UNKNOWN'
}

export interface IEquipment {
    id?: string;
    equipmentName?: string;
    equipmentDescription?: string;
    equipmentType?: EquipmentType;
    equipmentCoordinates?: string;
    equipmentSymbol?: string;
    equipmentImageContentType?: string;
    equipmentImage?: any;
    externalId?: string;
}

export class Equipment implements IEquipment {
    constructor(
        public id?: string,
        public equipmentName?: string,
        public equipmentDescription?: string,
        public equipmentType?: EquipmentType,
        public equipmentCoordinates?: string,
        public equipmentSymbol?: string,
        public equipmentImageContentType?: string,
        public equipmentImage?: any,
        public externalId?: string
    ) {}
}

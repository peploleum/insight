/**
 * Created by gFolgoas on 27/02/2019.
 */
import { IBiographics } from './biographics.model';
import { IEquipment } from './equipment.model';
import { IEvent } from './event.model';
import { ILocation } from './location.model';
import { IOrganisation } from './organisation.model';
import { IRawData } from './raw-data.model';
export class GenericModel implements IBiographics, IEquipment, IEvent, ILocation, IOrganisation, IRawData {
    constructor() {}
}

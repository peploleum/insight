import { IDictionary } from 'app/shared/model/analytics.model';

export interface IDictionaryContainer {
    dictionary: IDictionary;
    selected: boolean;
    modified: boolean;
}

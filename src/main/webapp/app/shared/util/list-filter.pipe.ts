/**
 * Created by gFolgoas on 07/02/2019.
 */
import { Pipe, PipeTransform } from '@angular/core';

/**
 * Filtre la liste en input sur la valeur de la propriété des objets spécifiée
 * */
@Pipe({ name: 'listFilterPipe' })
export class ListFilterPipe implements PipeTransform {
    transform(value: any[], propertyFiltered: string, valueFiltered: any): any[] {
        if (value == null || typeof value === 'undefined') {
            return null;
        }
        return value.filter(i => {
            return i[propertyFiltered] === valueFiltered;
        });
    }
}

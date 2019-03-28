/**
 * Created by GFOLGOAS on 28/03/2019.
 */
import { Pipe, PipeTransform } from '@angular/core';

/**
 * Order la liste en input sur la valeur de la propriété des objets spécifiée
 * */
@Pipe({ name: 'insOrderListByValuePipe' })
export class OrderListByValuePipe implements PipeTransform {
    transform(list: any[], targetProperty: string, valueOrder: any[]): any[] {
        if (list == null || typeof list === 'undefined') {
            return null;
        }
        const subLists: any[][] = valueOrder.map(value => {
            return list.filter(i => i[targetProperty] === value);
        });
        return subLists.reduce((x, y) => x.concat(y));
    }
}

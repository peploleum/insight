/**
 * Created by gFolgoas on 16/01/2019.
 */
/* tslint:disable:no-bitwise */
export const UUID = (): string => {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
        const r = (Math.random() * 16) | 0,
            v = c === 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
};
/* tslint:enable:no-bitwise */

/**
 * Fix pour une erreur de typescript sur EventTarget (property result manquante)
 * */
export interface FileReaderEventTarget extends EventTarget {
    result: string;
}

export class ToolbarButtonParameters {
    action: string;
    icon: string;
    tooltip: string;
    isToggled?: boolean;
    subButtons?: ToolbarButtonParameters[];

    constructor(action: string, icon: string, tooltip: string, isToggled?: boolean, subButtons?: ToolbarButtonParameters[]) {
        this.action = action;
        this.icon = icon;
        this.tooltip = tooltip;
        this.isToggled = isToggled;
        this.subButtons = subButtons;
    }
}

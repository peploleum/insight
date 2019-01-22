/**
 * Created by gFolgoas on 22/01/2019.
 */
export class MapState {
    DISPLAY_LABEL: boolean;
    DISPLAY_CONTENT_ON_HOVER: boolean;

    constructor(DISPLAY_LABEL: boolean, DISPLAY_CONTENT_ON_HOVER: boolean) {
        this.DISPLAY_LABEL = DISPLAY_LABEL;
        this.DISPLAY_CONTENT_ON_HOVER = DISPLAY_CONTENT_ON_HOVER;
    }
}

/**
 * Created by gFolgoas on 25/01/2019.
 */
export abstract class SideInterface {
    isHidden = true;

    displaySidePannel(doClose: boolean) {
        this.isHidden = doClose;
    }
}

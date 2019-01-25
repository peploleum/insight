/**
 * Created by gFolgoas on 25/01/2019.
 */
export abstract class SideInterface {
    isVisible = true;

    displaySidePannel() {
        this.isVisible = !this.isVisible;
    }
}

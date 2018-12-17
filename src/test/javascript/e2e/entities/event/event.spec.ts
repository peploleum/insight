/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EventComponentsPage, EventDeleteDialog, EventUpdatePage } from './event.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Event e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let eventUpdatePage: EventUpdatePage;
    let eventComponentsPage: EventComponentsPage;
    let eventDeleteDialog: EventDeleteDialog;
    const fileNameToUpload = 'logo-jhipster.png';
    const fileToUpload = '../../../../../main/webapp/content/images/' + fileNameToUpload;
    const absolutePath = path.resolve(__dirname, fileToUpload);

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Events', async () => {
        await navBarPage.goToEntity('event');
        eventComponentsPage = new EventComponentsPage();
        expect(await eventComponentsPage.getTitle()).to.eq('insightApp.event.home.title');
    });

    it('should load create Event page', async () => {
        await eventComponentsPage.clickOnCreateButton();
        eventUpdatePage = new EventUpdatePage();
        expect(await eventUpdatePage.getPageTitle()).to.eq('insightApp.event.home.createOrEditLabel');
        await eventUpdatePage.cancel();
    });

    it('should create and save Events', async () => {
        const nbButtonsBeforeCreate = await eventComponentsPage.countDeleteButtons();

        await eventComponentsPage.clickOnCreateButton();
        await promise.all([
            eventUpdatePage.setEventNameInput('eventName'),
            eventUpdatePage.setEventDescriptionInput('eventDescription'),
            eventUpdatePage.eventTypeSelectLastOption(),
            eventUpdatePage.setEventDateInput('2000-12-31'),
            eventUpdatePage.setEventCoordinatesInput('eventCoordinates'),
            eventUpdatePage.setEventImageInput(absolutePath),
            eventUpdatePage.setEventSymbolInput('eventSymbol')
        ]);
        expect(await eventUpdatePage.getEventNameInput()).to.eq('eventName');
        expect(await eventUpdatePage.getEventDescriptionInput()).to.eq('eventDescription');
        expect(await eventUpdatePage.getEventDateInput()).to.eq('2000-12-31');
        expect(await eventUpdatePage.getEventCoordinatesInput()).to.eq('eventCoordinates');
        expect(await eventUpdatePage.getEventImageInput()).to.endsWith(fileNameToUpload);
        expect(await eventUpdatePage.getEventSymbolInput()).to.eq('eventSymbol');
        await eventUpdatePage.save();
        expect(await eventUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await eventComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Event', async () => {
        const nbButtonsBeforeDelete = await eventComponentsPage.countDeleteButtons();
        await eventComponentsPage.clickOnLastDeleteButton();

        eventDeleteDialog = new EventDeleteDialog();
        expect(await eventDeleteDialog.getDialogTitle()).to.eq('insightApp.event.delete.question');
        await eventDeleteDialog.clickOnConfirmButton();

        expect(await eventComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});

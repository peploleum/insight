import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EventComponentsPage, EventUpdatePage } from './event.page-object';

describe('Event e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let eventUpdatePage: EventUpdatePage;
    let eventComponentsPage: EventComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Events', async () => {
        await navBarPage.goToEntity('event');
        eventComponentsPage = new EventComponentsPage();
        expect(await eventComponentsPage.getTitle()).toMatch(/insightApp.event.home.title/);
    });

    it('should load create Event page', async () => {
        await eventComponentsPage.clickOnCreateButton();
        eventUpdatePage = new EventUpdatePage();
        expect(await eventUpdatePage.getPageTitle()).toMatch(/insightApp.event.home.createOrEditLabel/);
        await eventUpdatePage.cancel();
    });

    it('should create and save Events', async () => {
        await eventComponentsPage.clickOnCreateButton();
        await eventUpdatePage.setEventNameInput('eventName');
        expect(await eventUpdatePage.getEventNameInput()).toMatch('eventName');
        await eventUpdatePage.setEventDescriptionInput('eventDescription');
        expect(await eventUpdatePage.getEventDescriptionInput()).toMatch('eventDescription');
        await eventUpdatePage.eventTypeSelectLastOption();
        await eventUpdatePage.setEventCoordinatesInput('eventCoordinates');
        expect(await eventUpdatePage.getEventCoordinatesInput()).toMatch('eventCoordinates');
        // eventUpdatePage.equipmentSelectLastOption();
        // eventUpdatePage.locationSelectLastOption();
        // eventUpdatePage.organisationSelectLastOption();
        await eventUpdatePage.save();
        expect(await eventUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});

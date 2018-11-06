import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ActorComponentsPage, ActorUpdatePage } from './actor.page-object';

describe('Actor e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let actorUpdatePage: ActorUpdatePage;
    let actorComponentsPage: ActorComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Actors', async () => {
        await navBarPage.goToEntity('actor');
        actorComponentsPage = new ActorComponentsPage();
        expect(await actorComponentsPage.getTitle()).toMatch(/insightApp.actor.home.title/);
    });

    it('should load create Actor page', async () => {
        await actorComponentsPage.clickOnCreateButton();
        actorUpdatePage = new ActorUpdatePage();
        expect(await actorUpdatePage.getPageTitle()).toMatch(/insightApp.actor.home.createOrEditLabel/);
        await actorUpdatePage.cancel();
    });

    it('should create and save Actors', async () => {
        await actorComponentsPage.clickOnCreateButton();
        await actorUpdatePage.setDescriptionInput('description');
        expect(await actorUpdatePage.getDescriptionInput()).toMatch('description');
        await actorUpdatePage.setNomInput('nom');
        expect(await actorUpdatePage.getNomInput()).toMatch('nom');
        await actorUpdatePage.setTypeInput('type');
        expect(await actorUpdatePage.getTypeInput()).toMatch('type');
        await actorUpdatePage.setLibelleInput('libelle');
        expect(await actorUpdatePage.getLibelleInput()).toMatch('libelle');
        await actorUpdatePage.setClasseIdentiteInput('classeIdentite');
        expect(await actorUpdatePage.getClasseIdentiteInput()).toMatch('classeIdentite');
        await actorUpdatePage.linkOfSelectLastOption();
        await actorUpdatePage.save();
        expect(await actorUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});

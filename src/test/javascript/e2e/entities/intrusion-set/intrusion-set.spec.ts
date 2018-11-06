import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IntrusionSetComponentsPage, IntrusionSetUpdatePage } from './intrusion-set.page-object';

describe('IntrusionSet e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let intrusionSetUpdatePage: IntrusionSetUpdatePage;
    let intrusionSetComponentsPage: IntrusionSetComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load IntrusionSets', async () => {
        await navBarPage.goToEntity('intrusion-set');
        intrusionSetComponentsPage = new IntrusionSetComponentsPage();
        expect(await intrusionSetComponentsPage.getTitle()).toMatch(/insightApp.intrusionSet.home.title/);
    });

    it('should load create IntrusionSet page', async () => {
        await intrusionSetComponentsPage.clickOnCreateButton();
        intrusionSetUpdatePage = new IntrusionSetUpdatePage();
        expect(await intrusionSetUpdatePage.getPageTitle()).toMatch(/insightApp.intrusionSet.home.createOrEditLabel/);
        await intrusionSetUpdatePage.cancel();
    });

    it('should create and save IntrusionSets', async () => {
        await intrusionSetComponentsPage.clickOnCreateButton();
        await intrusionSetUpdatePage.setDescriptionInput('description');
        expect(await intrusionSetUpdatePage.getDescriptionInput()).toMatch('description');
        await intrusionSetUpdatePage.setNomInput('nom');
        expect(await intrusionSetUpdatePage.getNomInput()).toMatch('nom');
        await intrusionSetUpdatePage.setTypeInput('type');
        expect(await intrusionSetUpdatePage.getTypeInput()).toMatch('type');
        await intrusionSetUpdatePage.setObjectifInput('objectif');
        expect(await intrusionSetUpdatePage.getObjectifInput()).toMatch('objectif');
        await intrusionSetUpdatePage.setNiveauRessourceInput('niveauRessource');
        expect(await intrusionSetUpdatePage.getNiveauRessourceInput()).toMatch('niveauRessource');
        await intrusionSetUpdatePage.isTargetsIntrusionSetToActorSelectLastOption();
        await intrusionSetUpdatePage.linkOfSelectLastOption();
        await intrusionSetUpdatePage.save();
        expect(await intrusionSetUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});

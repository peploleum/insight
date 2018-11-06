import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ToolComponentsPage, ToolUpdatePage } from './tool.page-object';

describe('Tool e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let toolUpdatePage: ToolUpdatePage;
    let toolComponentsPage: ToolComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Tools', async () => {
        await navBarPage.goToEntity('tool');
        toolComponentsPage = new ToolComponentsPage();
        expect(await toolComponentsPage.getTitle()).toMatch(/insightApp.tool.home.title/);
    });

    it('should load create Tool page', async () => {
        await toolComponentsPage.clickOnCreateButton();
        toolUpdatePage = new ToolUpdatePage();
        expect(await toolUpdatePage.getPageTitle()).toMatch(/insightApp.tool.home.createOrEditLabel/);
        await toolUpdatePage.cancel();
    });

    it('should create and save Tools', async () => {
        await toolComponentsPage.clickOnCreateButton();
        await toolUpdatePage.setNomInput('nom');
        expect(await toolUpdatePage.getNomInput()).toMatch('nom');
        await toolUpdatePage.setTypeInput('type');
        expect(await toolUpdatePage.getTypeInput()).toMatch('type');
        await toolUpdatePage.setLibelleInput('libelle');
        expect(await toolUpdatePage.getLibelleInput()).toMatch('libelle');
        await toolUpdatePage.setDescriptionInput('description');
        expect(await toolUpdatePage.getDescriptionInput()).toMatch('description');
        await toolUpdatePage.setVersionInput('version');
        expect(await toolUpdatePage.getVersionInput()).toMatch('version');
        await toolUpdatePage.usesToolToIntrusionSetSelectLastOption();
        await toolUpdatePage.isUsesToolToMalwareSelectLastOption();
        await toolUpdatePage.linkOfSelectLastOption();
        await toolUpdatePage.usesToolToThreatActorSelectLastOption();
        await toolUpdatePage.save();
        expect(await toolUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});

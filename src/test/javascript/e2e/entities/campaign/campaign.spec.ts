import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CampaignComponentsPage, CampaignUpdatePage } from './campaign.page-object';

describe('Campaign e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let campaignUpdatePage: CampaignUpdatePage;
    let campaignComponentsPage: CampaignComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Campaigns', async () => {
        await navBarPage.goToEntity('campaign');
        campaignComponentsPage = new CampaignComponentsPage();
        expect(await campaignComponentsPage.getTitle()).toMatch(/insightApp.campaign.home.title/);
    });

    it('should load create Campaign page', async () => {
        await campaignComponentsPage.clickOnCreateButton();
        campaignUpdatePage = new CampaignUpdatePage();
        expect(await campaignUpdatePage.getPageTitle()).toMatch(/insightApp.campaign.home.createOrEditLabel/);
        await campaignUpdatePage.cancel();
    });

    it('should create and save Campaigns', async () => {
        await campaignComponentsPage.clickOnCreateButton();
        await campaignUpdatePage.setDescriptionInput('description');
        expect(await campaignUpdatePage.getDescriptionInput()).toMatch('description');
        await campaignUpdatePage.setNomInput('nom');
        expect(await campaignUpdatePage.getNomInput()).toMatch('nom');
        await campaignUpdatePage.setObjectifInput('objectif');
        expect(await campaignUpdatePage.getObjectifInput()).toMatch('objectif');
        await campaignUpdatePage.setAliasInput('alias');
        expect(await campaignUpdatePage.getAliasInput()).toMatch('alias');
        await campaignUpdatePage.setTypeInput('type');
        expect(await campaignUpdatePage.getTypeInput()).toMatch('type');
        await campaignUpdatePage.linkOfSelectLastOption();
        await campaignUpdatePage.save();
        expect(await campaignUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});

/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RawDataComponentsPage, RawDataDeleteDialog, RawDataUpdatePage } from './raw-data.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('RawData e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let rawDataUpdatePage: RawDataUpdatePage;
    let rawDataComponentsPage: RawDataComponentsPage;
    let rawDataDeleteDialog: RawDataDeleteDialog;
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

    it('should load RawData', async () => {
        await navBarPage.goToEntity('raw-data');
        rawDataComponentsPage = new RawDataComponentsPage();
        expect(await rawDataComponentsPage.getTitle()).to.eq('insightApp.rawData.home.title');
    });

    it('should load create RawData page', async () => {
        await rawDataComponentsPage.clickOnCreateButton();
        rawDataUpdatePage = new RawDataUpdatePage();
        expect(await rawDataUpdatePage.getPageTitle()).to.eq('insightApp.rawData.home.createOrEditLabel');
        await rawDataUpdatePage.cancel();
    });

    it('should create and save RawData', async () => {
        const nbButtonsBeforeCreate = await rawDataComponentsPage.countDeleteButtons();

        await rawDataComponentsPage.clickOnCreateButton();
        await promise.all([
            rawDataUpdatePage.setRawDataNameInput('rawDataName'),
            rawDataUpdatePage.setRawDataTypeInput('rawDataType'),
            rawDataUpdatePage.setRawDataSubTypeInput('rawDataSubType'),
            rawDataUpdatePage.setRawDataSourceNameInput('rawDataSourceName'),
            rawDataUpdatePage.setRawDataSourceUriInput('rawDataSourceUri'),
            rawDataUpdatePage.setRawDataSourceTypeInput('rawDataSourceType'),
            rawDataUpdatePage.setRawDataContentInput('rawDataContent'),
            rawDataUpdatePage.setRawDataCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            rawDataUpdatePage.setRawDataExtractedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            rawDataUpdatePage.setRawDataSymbolInput('rawDataSymbol'),
            rawDataUpdatePage.setRawDataDataInput(absolutePath),
            rawDataUpdatePage.setRawDataCoordinatesInput('rawDataCoordinates'),
            rawDataUpdatePage.setRawDataAnnotationsInput('rawDataAnnotations'),
            rawDataUpdatePage.setExternalIdInput('externalId')
        ]);
        expect(await rawDataUpdatePage.getRawDataNameInput()).to.eq('rawDataName');
        expect(await rawDataUpdatePage.getRawDataTypeInput()).to.eq('rawDataType');
        expect(await rawDataUpdatePage.getRawDataSubTypeInput()).to.eq('rawDataSubType');
        expect(await rawDataUpdatePage.getRawDataSourceNameInput()).to.eq('rawDataSourceName');
        expect(await rawDataUpdatePage.getRawDataSourceUriInput()).to.eq('rawDataSourceUri');
        expect(await rawDataUpdatePage.getRawDataSourceTypeInput()).to.eq('rawDataSourceType');
        expect(await rawDataUpdatePage.getRawDataContentInput()).to.eq('rawDataContent');
        expect(await rawDataUpdatePage.getRawDataCreationDateInput()).to.contain('2001-01-01T02:30');
        expect(await rawDataUpdatePage.getRawDataExtractedDateInput()).to.contain('2001-01-01T02:30');
        expect(await rawDataUpdatePage.getRawDataSymbolInput()).to.eq('rawDataSymbol');
        expect(await rawDataUpdatePage.getRawDataDataInput()).to.endsWith(fileNameToUpload);
        expect(await rawDataUpdatePage.getRawDataCoordinatesInput()).to.eq('rawDataCoordinates');
        expect(await rawDataUpdatePage.getRawDataAnnotationsInput()).to.eq('rawDataAnnotations');
        expect(await rawDataUpdatePage.getExternalIdInput()).to.eq('externalId');
        await rawDataUpdatePage.save();
        expect(await rawDataUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await rawDataComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last RawData', async () => {
        const nbButtonsBeforeDelete = await rawDataComponentsPage.countDeleteButtons();
        await rawDataComponentsPage.clickOnLastDeleteButton();

        rawDataDeleteDialog = new RawDataDeleteDialog();
        expect(await rawDataDeleteDialog.getDialogTitle()).to.eq('insightApp.rawData.delete.question');
        await rawDataDeleteDialog.clickOnConfirmButton();

        expect(await rawDataComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});

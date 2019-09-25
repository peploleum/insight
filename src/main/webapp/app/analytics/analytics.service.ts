import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBiographics } from 'app/shared/model/biographics.model';
import { RawData } from '../shared/model/raw-data.model';
import { IdType } from 'vis';
import { IGraphyNodeDTO } from '../shared/model/node.model';
import { map } from 'rxjs/internal/operators';
import { DictionaryDTO, ScoreDTO, Theme } from '../shared/model/analytics.model';

type EntityResponseType = HttpResponse<IBiographics>;
type EntityArrayResponseType = HttpResponse<IBiographics[]>;

@Injectable({ providedIn: 'root' })
export class AnalyticsService {
    public resourceUrl = SERVER_API_URL + 'api/biographics';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/biographics';
    private _resourceUrl = SERVER_API_URL + 'api/graph/';
    private _ressourceDictionaryUrl = SERVER_API_URL + 'api/dictionary/';

    public static getScoreDTO(
        idBio?: string,
        scorePoints?: string,
        scoreListMotsClefs?: { theme: Theme; motClef: string }[],
        scoreImageHit?: string,
        scoreFrequence?: string
    ): ScoreDTO {
        return new ScoreDTO(idBio, scorePoints, scoreListMotsClefs, scoreImageHit, scoreFrequence);
    }

    constructor(protected http: HttpClient) {}

    create(biographics: IBiographics): Observable<EntityResponseType> {
        return this.http.post<IBiographics>(this.resourceUrl, biographics, { observe: 'response' });
    }

    update(biographics: IBiographics): Observable<EntityResponseType> {
        return this.http.put<IBiographics>(this.resourceUrl, biographics, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IBiographics>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBiographics[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBiographics[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    getRawDataUrLCollection(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBiographics[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
        // effectue un get sur this.resourceSearchUrl avec des arguments
    }

    postFile(fileToUpload: File): Observable<EntityResponseType> {
        const headers = new HttpHeaders();
        const formData: FormData = new FormData();
        formData.append('fileKey', fileToUpload, fileToUpload.name);
        let dico = new DictionaryDTO();

        console.log(formData);
        return this.http.post(this._ressourceDictionaryUrl, dico, { headers: headers, observe: 'response' });
    }

    getScores(janusIdOrigin: IdType): Observable<ScoreDTO[]> {
        const headers = new HttpHeaders();
        return this.http
            .get(`${this._resourceUrl}` + 'traversal/' + `${janusIdOrigin}`, {
                headers,
                observe: 'response'
            })
            .pipe(
                map((res: HttpResponse<IGraphyNodeDTO[]>) => {
                    const body: IGraphyNodeDTO[] = res.body; // vrai noeud avec les props
                    const data: ScoreDTO[] = body
                        .map((item: IGraphyNodeDTO) => {
                            if (item.type === 'RawData' && item.properties) {
                                return AnalyticsService.getScoreDTO(
                                    janusIdOrigin as string,
                                    item.properties.points as string,
                                    (item.properties.listMotsClefs as string[]).map(str => {
                                        const parts = str.split('.');
                                        return { theme: parts[0] as Theme, motClef: parts[1] };
                                    }),
                                    item.properties.imageHit as string,
                                    item.properties.frequence as string
                                );
                            } else {
                                return null;
                            }
                        })
                        .filter(s => s !== null);
                    return data;
                }),
                map((score: ScoreDTO[]) => {
                    if (score === null) {
                        return null;
                    } else {
                        return score;
                    }
                })
            );
    }
}

export const FAKE_SCORE: ScoreDTO[] = [
    {
        idBio: '',
        scorePoints: '',
        scoreListMotsClefs: [
            { theme: 'TER', motClef: '' },
            { theme: 'TER', motClef: '' },
            { theme: 'TER', motClef: '' },
            { theme: 'TER', motClef: '' },
            { theme: 'TER', motClef: '' },
            { theme: 'TER', motClef: '' }
        ],
        scoreImageHit: '',
        scoreFrequence: ''
    }
];

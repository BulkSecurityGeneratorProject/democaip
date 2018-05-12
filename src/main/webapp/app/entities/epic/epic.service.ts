import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Epic } from './epic.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Epic>;

@Injectable()
export class EpicService {

    private resourceUrl =  SERVER_API_URL + 'api/epics';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(epic: Epic): Observable<EntityResponseType> {
        const copy = this.convert(epic);
        return this.http.post<Epic>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(epic: Epic): Observable<EntityResponseType> {
        const copy = this.convert(epic);
        return this.http.put<Epic>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Epic>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Epic[]>> {
        const options = createRequestOption(req);
        return this.http.get<Epic[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Epic[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Epic = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Epic[]>): HttpResponse<Epic[]> {
        const jsonResponse: Epic[] = res.body;
        const body: Epic[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Epic.
     */
    private convertItemFromServer(epic: Epic): Epic {
        const copy: Epic = Object.assign({}, epic);
        copy.creation = this.dateUtils
            .convertLocalDateFromServer(epic.creation);
        return copy;
    }

    /**
     * Convert a Epic to a JSON which can be sent to the server.
     */
    private convert(epic: Epic): Epic {
        const copy: Epic = Object.assign({}, epic);
        copy.creation = this.dateUtils
            .convertLocalDateToServer(epic.creation);
        return copy;
    }
}

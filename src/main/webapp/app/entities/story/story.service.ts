import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Story } from './story.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Story>;

@Injectable()
export class StoryService {

    private resourceUrl =  SERVER_API_URL + 'api/stories';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(story: Story): Observable<EntityResponseType> {
        const copy = this.convert(story);
        return this.http.post<Story>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(story: Story): Observable<EntityResponseType> {
        const copy = this.convert(story);
        return this.http.put<Story>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Story>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Story[]>> {
        const options = createRequestOption(req);
        return this.http.get<Story[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Story[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Story = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Story[]>): HttpResponse<Story[]> {
        const jsonResponse: Story[] = res.body;
        const body: Story[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Story.
     */
    private convertItemFromServer(story: Story): Story {
        const copy: Story = Object.assign({}, story);
        copy.creation = this.dateUtils
            .convertLocalDateFromServer(story.creation);
        return copy;
    }

    /**
     * Convert a Story to a JSON which can be sent to the server.
     */
    private convert(story: Story): Story {
        const copy: Story = Object.assign({}, story);
        copy.creation = this.dateUtils
            .convertLocalDateToServer(story.creation);
        return copy;
    }
}

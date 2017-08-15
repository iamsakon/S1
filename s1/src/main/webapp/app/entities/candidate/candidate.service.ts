import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Candidate } from './candidate.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CandidateService {

    private resourceUrl = 'hcm/api/candidates';
    private resourceSearchUrl = 'hcm/api/_search/candidates';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(candidate: Candidate): Observable<Candidate> {
        const copy = this.convert(candidate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(candidate: Candidate): Observable<Candidate> {
        const copy = this.convert(candidate);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Candidate> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.birthDate = this.dateUtils
            .convertLocalDateFromServer(entity.birthDate);
    }

    private convert(candidate: Candidate): Candidate {
        const copy: Candidate = Object.assign({}, candidate);
        copy.birthDate = this.dateUtils
            .convertLocalDateToServer(candidate.birthDate);
        return copy;
    }
}

import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { ProbationPeriod } from './probation-period.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProbationPeriodService {

    private resourceUrl = 'hcm/api/probation-periods';
    private resourceSearchUrl = 'hcm/api/_search/probation-periods';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(probationPeriod: ProbationPeriod): Observable<ProbationPeriod> {
        const copy = this.convert(probationPeriod);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(probationPeriod: ProbationPeriod): Observable<ProbationPeriod> {
        const copy = this.convert(probationPeriod);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<ProbationPeriod> {
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
        entity.periodStart = this.dateUtils
            .convertLocalDateFromServer(entity.periodStart);
        entity.periodEnd = this.dateUtils
            .convertLocalDateFromServer(entity.periodEnd);
        entity.evaluatePeriodStart = this.dateUtils
            .convertLocalDateFromServer(entity.evaluatePeriodStart);
        entity.evaluatePeriodEnd = this.dateUtils
            .convertLocalDateFromServer(entity.evaluatePeriodEnd);
    }

    private convert(probationPeriod: ProbationPeriod): ProbationPeriod {
        const copy: ProbationPeriod = Object.assign({}, probationPeriod);
        copy.periodStart = this.dateUtils
            .convertLocalDateToServer(probationPeriod.periodStart);
        copy.periodEnd = this.dateUtils
            .convertLocalDateToServer(probationPeriod.periodEnd);
        copy.evaluatePeriodStart = this.dateUtils
            .convertLocalDateToServer(probationPeriod.evaluatePeriodStart);
        copy.evaluatePeriodEnd = this.dateUtils
            .convertLocalDateToServer(probationPeriod.evaluatePeriodEnd);
        return copy;
    }
}

import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { ProbationDay } from './probation-day.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProbationDayService {

    private resourceUrl = 'hcm/api/probation-days';
    private resourceSearchUrl = 'hcm/api/_search/probation-days';

    constructor(private http: Http) { }

    create(probationDay: ProbationDay): Observable<ProbationDay> {
        const copy = this.convert(probationDay);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(probationDay: ProbationDay): Observable<ProbationDay> {
        const copy = this.convert(probationDay);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<ProbationDay> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(probationDay: ProbationDay): ProbationDay {
        const copy: ProbationDay = Object.assign({}, probationDay);
        return copy;
    }
}

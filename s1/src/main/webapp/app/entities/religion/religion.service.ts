import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Religion } from './religion.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ReligionService {

    private resourceUrl = 'hcm/api/religions';
    private resourceSearchUrl = 'hcm/api/_search/religions';

    constructor(private http: Http) { }

    create(religion: Religion): Observable<Religion> {
        const copy = this.convert(religion);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(religion: Religion): Observable<Religion> {
        const copy = this.convert(religion);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Religion> {
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

    private convert(religion: Religion): Religion {
        const copy: Religion = Object.assign({}, religion);
        return copy;
    }
}

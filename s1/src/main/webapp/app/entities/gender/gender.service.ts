import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Gender } from './gender.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class GenderService {

    private resourceUrl = 'hcm/api/genders';
    private resourceSearchUrl = 'hcm/api/_search/genders';

    constructor(private http: Http) { }

    create(gender: Gender): Observable<Gender> {
        const copy = this.convert(gender);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(gender: Gender): Observable<Gender> {
        const copy = this.convert(gender);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Gender> {
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

    private convert(gender: Gender): Gender {
        const copy: Gender = Object.assign({}, gender);
        return copy;
    }
}

import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { BloodType } from './blood-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class BloodTypeService {

    private resourceUrl = 'hcm/api/blood-types';
    private resourceSearchUrl = 'hcm/api/_search/blood-types';

    constructor(private http: Http) { }

    create(bloodType: BloodType): Observable<BloodType> {
        const copy = this.convert(bloodType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(bloodType: BloodType): Observable<BloodType> {
        const copy = this.convert(bloodType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<BloodType> {
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

    private convert(bloodType: BloodType): BloodType {
        const copy: BloodType = Object.assign({}, bloodType);
        return copy;
    }
}

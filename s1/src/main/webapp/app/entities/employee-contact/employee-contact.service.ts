import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { EmployeeContact } from './employee-contact.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EmployeeContactService {

    private resourceUrl = 'hcm/api/employee-contacts';
    private resourceSearchUrl = 'hcm/api/_search/employee-contacts';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(employeeContact: EmployeeContact): Observable<EmployeeContact> {
        const copy = this.convert(employeeContact);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(employeeContact: EmployeeContact): Observable<EmployeeContact> {
        const copy = this.convert(employeeContact);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<EmployeeContact> {
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
        entity.activeDate = this.dateUtils
            .convertLocalDateFromServer(entity.activeDate);
    }

    private convert(employeeContact: EmployeeContact): EmployeeContact {
        const copy: EmployeeContact = Object.assign({}, employeeContact);
        copy.activeDate = this.dateUtils
            .convertLocalDateToServer(employeeContact.activeDate);
        return copy;
    }
}

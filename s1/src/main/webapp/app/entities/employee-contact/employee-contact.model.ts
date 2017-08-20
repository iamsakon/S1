import { BaseEntity } from './../../shared';

export class EmployeeContact implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public companyCode?: number,
        public email?: string,
        public mobileNumber?: string,
        public phoneNumber?: string,
        public phoneNoExtension?: string,
        public activeDate?: any,
        public employee?: BaseEntity,
    ) {
    }
}

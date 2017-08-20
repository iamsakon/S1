import { BaseEntity } from './../../shared';

export class Employee implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public companyCode?: number,
        public employeeCode?: string,
        public firstName?: string,
        public middleName?: string,
        public lastName?: string,
        public birthDate?: any,
        public joinedDate?: any,
        public employeeContacts?: BaseEntity[],
        public probationPeriods?: BaseEntity[],
        public prefix?: BaseEntity,
        public gender?: BaseEntity,
        public bloodType?: BaseEntity,
        public nationality?: BaseEntity,
        public race?: BaseEntity,
        public religion?: BaseEntity,
        public militaryStatus?: BaseEntity,
        public maritalStatus?: BaseEntity,
    ) {
    }
}

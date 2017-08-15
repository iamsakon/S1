import { BaseEntity } from './../../shared';

export class Candidate implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public firstName?: string,
        public middleName?: string,
        public lastName?: string,
        public birthDate?: any,
        public personalEmail?: string,
        public mobileNumber?: string,
        public phoneNumber?: string,
        public activeFlag?: boolean,
        public tenantId?: number,
        public companyCode?: number,
        public prefix?: BaseEntity,
        public gender?: BaseEntity,
        public bloodType?: BaseEntity,
        public nationality?: BaseEntity,
        public race?: BaseEntity,
        public religion?: BaseEntity,
        public militaryStatus?: BaseEntity,
        public maritalStatus?: BaseEntity,
    ) {
        this.activeFlag = false;
    }
}

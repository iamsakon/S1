import { BaseEntity } from './../../shared';

export class Prefix implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public description?: string,
        public activeFlag?: boolean,
        public tenantId?: number,
        public companyCode?: number,
    ) {
        this.activeFlag = false;
    }
}

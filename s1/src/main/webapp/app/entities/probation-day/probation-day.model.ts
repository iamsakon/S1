import { BaseEntity } from './../../shared';

export class ProbationDay implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public companyCode?: number,
        public amountDate?: number,
    ) {
    }
}

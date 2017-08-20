import { BaseEntity } from './../../shared';

export class ProbationPeriod implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public companyCode?: number,
        public periodStart?: any,
        public periodEnd?: any,
        public evaluatePeriodStart?: any,
        public evaluatePeriodEnd?: any,
        public probation?: BaseEntity,
        public employee?: BaseEntity,
    ) {
    }
}

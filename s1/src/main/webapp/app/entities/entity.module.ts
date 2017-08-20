import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { S1PrefixModule } from './prefix/prefix.module';
import { S1NationalityModule } from './nationality/nationality.module';
import { S1RaceModule } from './race/race.module';
import { S1ReligionModule } from './religion/religion.module';
import { S1GenderModule } from './gender/gender.module';
import { S1MaritalStatusModule } from './marital-status/marital-status.module';
import { S1MilitaryStatusModule } from './military-status/military-status.module';
import { S1BloodTypeModule } from './blood-type/blood-type.module';
import { S1EmployeeModule } from './employee/employee.module';
import { S1ProbationDayModule } from './probation-day/probation-day.module';
import { S1ProbationPeriodModule } from './probation-period/probation-period.module';
import { S1EmployeeContactModule } from './employee-contact/employee-contact.module';
import { S1CandidateModule } from './candidate/candidate.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        S1PrefixModule,
        S1NationalityModule,
        S1RaceModule,
        S1ReligionModule,
        S1GenderModule,
        S1MaritalStatusModule,
        S1MilitaryStatusModule,
        S1BloodTypeModule,
        S1EmployeeModule,
        S1ProbationDayModule,
        S1ProbationPeriodModule,
        S1EmployeeContactModule,
        S1CandidateModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1EntityModule {}

export class CompaniesWithWorkers {
    companyId: number = 0;
    name: string = "";
    address: string = "";
    rating: number = 0.0;
    workers: Array<{
        name: string;
        lastname: string;
    }> = [];
}